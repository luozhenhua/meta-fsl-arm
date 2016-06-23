# Provides the QorIQ common settings for ARM
require conf/machine/include/soc-family.inc

KERNEL_IMAGETYPE ?= "uImage"

MACHINEOVERRIDES =. "qoriq-arm:"

require conf/machine/include/qoriq.inc
