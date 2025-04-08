# MySpringBootApp - AWS-Powered Image Upload Application

This project is a Spring Boot application that allows users to upload images to Amazon S3 and view a dashboard of uploaded images. The application is deployed on AWS using a blue/green deployment strategy with ECS, CodeDeploy, and other AWS services. This README provides an overview of the application, and the AWS resources used. 

## Table of Contents
- [Project Overview](#project-overview)
- [AWS Resources Used](#aws-resources-used)
- [Architecture Diagram](#architecture-diagram)

## Project Overview
`MySpringBootApp` is a web application built with Spring Boot that provides the following features:
- **Image Upload**: Users can upload images, which are stored in an Amazon S3 bucket.
- **Dashboard**: Users can view a dashboard listing all uploaded images.
- **Blue/Green Deployment**: The application is deployed on AWS ECS with a blue/green deployment strategy using CodeDeploy, ensuring zero-downtime updates.

The application is containerized using Docker and deployed on AWS ECS Fargate, with a robust CI/CD pipeline implemented via GitHub Actions.

## AWS Resources Used
This project leverages several AWS services to build a scalable, secure, and highly available application. Below is a detailed breakdown of the AWS resources used:

- **Amazon VPC**:
  - A custom VPC with 2 public subnets (for the ALB) and 2 private subnets (for ECS tasks).
  - Internet Gateway for public access.
  - NAT Gateway in the public subnets to allow private subnets to access the internet ( for pulling Docker images and accessing S3).
  - Route tables and subnet associations to manage traffic flow.

- **Application Load Balancer (ALB)**:
  - An ALB in the public subnets to distribute traffic to ECS tasks.
  - Listener on port 80 (HTTP) to handle incoming requests.
  - Two target groups (`my-target-group1` and `my-target-group2`) for blue/green deployment, allowing CodeDeploy to switch traffic between the blue and green environments.

- **Amazon ECS (Elastic Container Service)**:
  - An ECS cluster (`MyECSCluster`) running in Fargate mode for serverless container management.
  - An ECS service (`MySpringBootService`) that manages the desired number of tasks (2 tasks for high availability).
  - Task definition (`my-springboot-app`) defining the container image, CPU, memory, and networking settings.
  - Network configuration using `awsvpc` mode, with tasks running in private subnets and associated with a security group.

- **AWS CodeDeploy**:
  - Used for blue/green deployment to ECS.
  - Application (`MySpringBootApp`) and deployment group (`MyDeploymentGroup`) configured for ECS blue/green deployment.
  - Automatically switches traffic between `my-target-group1` (blue) and `my-target-group2` (green) during deployments.
  - Deployment configuration (`CodeDeployDefault.ECSAllAtOnce`) to deploy all tasks at once.
  - Original revision termination set to 1 hour, allowing time for rollback if needed.

- **Amazon S3**:
  - A bucket (`my-image-upload-bucket-<account-id>`) to store uploaded images.
  - Configured with appropriate IAM permissions for the application to read/write images.

- **Amazon ECR (Elastic Container Registry)**:
  - A public ECR repository to store the Docker image of the application.
  - The GitHub Actions workflow builds and pushes the image to ECR with a unique tag.

- **IAM Roles and Policies**:
  - **ECS Task Execution Role**: Allows ECS tasks to pull images from ECR and write logs to CloudWatch.
  - **ECS Task Role**: Grants the application permissions to read/write to the S3 bucket.
  - **CodeDeploy Role**: Grants CodeDeploy permissions to manage ECS deployments (`AWSCodeDeployRoleForECS` policy).
  - **GitHub Actions Role**: An IAM user/role with permissions to push to ECR, manage ECS task definitions, and trigger CodeDeploy deployments.

- **Security Groups**:
  - ALB security group: Allows inbound HTTP traffic on port 80 from the internet.
  - ECS security group: Allows inbound traffic on port 8080 from the ALB security group.

- **CloudFormation**:
  - Two CloudFormation stacks to provision the infrastructure:
    - `VPCStack`: Creates the VPC, subnets, ALB, target groups, and security groups.
    - `ECSStack`: Creates the ECS cluster, service, task definition, and associated resources.

## Architecture Diagram
The following diagram illustrates the network architecture of the application:
![network-architecture diagram](https://github.com/user-attachments/assets/a4f6e528-c4de-45b2-ac90-131da2e3ac02)

