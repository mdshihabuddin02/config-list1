provider "aws" {
  region = "us-west-2"  # Replace with your desired AWS region
}

locals {
  cluster_name = "my-eks-cluster"
}

resource "aws_vpc" "eks_vpc" {
  cidr_block = "10.0.0.0/16"
}

resource "aws_internet_gateway" "eks_igw" {
  vpc_id = aws_vpc.eks_vpc.id
}

resource "aws_subnet" "eks_subnet_1" {
  vpc_id            = aws_vpc.eks_vpc.id
  cidr_block        = "10.0.1.0/24"
  availability_zone = "us-west-2a"  # Replace with your desired availability zone
}

resource "aws_subnet" "eks_subnet_2" {
  vpc_id            = aws_vpc.eks_vpc.id
  cidr_block        = "10.0.2.0/24"
  availability_zone = "us-west-2b"  # Replace with your desired availability zone
}

resource "aws_security_group" "eks_cluster_sg" {
  name_prefix = "eks_cluster_sg_"

  ingress {
    from_port = 0
    to_port   = 65535
    protocol  = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

module "eks" {
  source = "terraform-aws-modules/eks/aws"

  cluster_name = local.cluster_name
  subnets      = [aws_subnet.eks_subnet_1.id, aws_subnet.eks_subnet_2.id]
  vpc_id       = aws_vpc.eks_vpc.id

  tags = {
    Terraform   = "true"
    Environment = "dev"
  }

  worker_groups_launch_template = [
    {
      name                 = "worker-group"
      instance_type        = "t2.micro"
      additional_security_group_ids = [aws_security_group.eks_cluster_sg.id]
    }
  ]
}

output "kubeconfig" {
  value = module.eks.kubeconfig
}

output "config_map_aws_auth" {
  value = module.eks.config_map_aws_auth
}
