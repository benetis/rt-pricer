#!/bin/sh

sbt assembly

scp ./target/scala-2.11/rt-prices-assembly-1.0.jar dev@hollywoo.local:~