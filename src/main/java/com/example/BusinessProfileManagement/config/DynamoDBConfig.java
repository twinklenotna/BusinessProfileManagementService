package com.example.BusinessProfileManagement.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

@Configuration
public class DynamoDBConfig {

  @Value("${amazon.dynamodb.endpoint}")
  private String dynamoDBEndpoint;

  @Value("${amazon.aws.accesskey}")
  private String awsAccessKey;

  @Value("${amazon.aws.secretkey}")
  private String awsSecretKey;

  @Value("${amazon.aws.region}")
  private String awsRegion;

  @Bean
  public AmazonDynamoDB amazonDynamoDB() {
    return AmazonDynamoDBClient.builder()
        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(dynamoDBEndpoint, awsRegion))
        .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
        .build();
  }

  @Bean
  public DynamoDBMapper dynamoDBMapper() {
    return new DynamoDBMapper(amazonDynamoDB());
  }
}
