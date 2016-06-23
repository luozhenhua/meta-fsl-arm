# Provides the QorIQ common settings

PREFERRED_PROVIDER_virtual/kernel ?= "linux-qoriq"
PREFERRED_PROVIDER_virtual/bootloader ?= "u-boot-qoriq"
PREFERRED_PROVIDER_u-boot ?= "u-boot-qoriq"

MACHINE_EXTRA_RRECOMMENDS += "udev-extraconf kernel-modules"

IMAGE_CLASSES += "image_types_uboot"
EXTRA_IMAGEDEPENDS += "u-boot"

MACHINEOVERRIDES =. "qoriq:"
