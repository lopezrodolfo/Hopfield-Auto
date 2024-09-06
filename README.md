# Hopfield Autoassociative Neural Network

## Description

This project implements a Hopfield Autoassociative Neural Network for pattern recognition and completion. The network can be trained on a set of patterns and then used to recognize or complete partial or noisy versions of those patterns.

## Authors

- Rodolfo Lopez
- Andrew Kirrane
- Julia Paley

## Date

11/10/2021

## Features

- Train the network using a training data file
- Save and load trained weight settings
- Test/deploy the network using a testing/deploying data file
- Save testing/deploying results

## Usage

1. Run the program
2. Choose to either train the network or load pre-trained weights
3. If training:
   - Provide a training data file
   - Save the trained weights to a file
4. Choose to test/deploy or quit
5. If testing/deploying:
   - Provide a testing/deploying data file
   - Save the results to a file

## File Formats

- Training data file: Contains sample images for training
- Weight settings file: Stores the trained network weights
- Testing/deploying data file: Contains images for testing or deployment
- Results file: Stores the output of testing/deployment

## Implementation Details

The project is implemented in Java and includes the following main components:

- `proj2.java`: Main driver class
- `Readinput.java`: Handles input file reading
- `train()`: Trains the network
- `test()`: Tests/deploys the network
- `writeweights()`: Saves trained weights
- `writeresults()`: Saves testing/deploying results

## Notes

- The network uses bipolar representation (-1 and 1) for patterns
- The weight matrix is symmetric with zero diagonal

For more details on the implementation and usage, please refer to the source code and comments.
