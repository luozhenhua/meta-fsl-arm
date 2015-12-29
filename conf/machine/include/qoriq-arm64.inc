# Provides the QorIQ common settings for ARM64
require conf/machine/include/soc-family.inc

KERNEL_IMAGETYPE ?= "Image"

BBMASK = ".*openjre|.*openjdk"

MACHINEOVERRIDES =. "qoriq-arm64:"

require conf/machine/include/qoriq.inc
