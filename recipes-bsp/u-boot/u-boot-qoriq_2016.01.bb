require recipes-bsp/u-boot/u-boot.inc
inherit fsl-u-boot-localversion

DESCRIPTION = "U-Boot provided by Freescale with focus on QorIQ boards"
PROVIDES += "u-boot"
LICENSE = "GPLv2 & BSD-3-Clause & BSD-2-Clause & LGPL-2.0 & LGPL-2.1"
LIC_FILES_CHKSUM = " \
    file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://Licenses/bsd-2-clause.txt;md5=6a31f076f5773aabd8ff86191ad6fdd5 \
    file://Licenses/bsd-3-clause.txt;md5=4a1190eac56a9db675d58ebe86eaf50c \
    file://Licenses/lgpl-2.0.txt;md5=5f30f0716dfdd0d91eb439ebec522ec2 \
    file://Licenses/lgpl-2.1.txt;md5=4fbd65380cdd255951079008b364516c \
"

DEPENDS_append = " change-file-endianess-native dtc-native tcl-native"

SRC_URI = "git://git.freescale.com/ppc/sdk/u-boot.git;branch=sdk-v2.0.x \
    file://fix-build-error-under-gcc6.patch \
"
SRCREV = "a9b437f50e2051f8d42ec9e1a6df52de4bc00e1e"

S = "${WORKDIR}/git"

LOCALVERSION ?= "+fsl"

do_compile_append () {
 unset i j
    if [ "x${UBOOT_CONFIG}" != "x" ]; then
        for config in ${UBOOT_MACHINE}; do
            i=`expr $i + 1`;
            for type in ${UBOOT_CONFIG}; do
                j=`expr $j + 1`;
                if [ $j -eq $i ]; then
                    case "${config}" in
                        *SECURE*|*SECBOOT*)
                            :;;
                        *nor*)
                            cp ${config}/u-boot-dtb.bin ${config}/u-boot-${type}.${UBOOT_SUFFIX};;
                        *nand* | *sdcard*)
                            cp ${config}/u-boot-with-spl-pbl.bin ${config}/u-boot-${type}.${UBOOT_SUFFIX};;
                        *spi*) 
                            tclsh ${STAGING_BINDIR_NATIVE}/byte_swap.tcl ${config}/u-boot-dtb.bin ${config}/u-boot.swap.bin 8
                            cp ${config}/u-boot.swap.bin ${config}/u-boot-${type}.${UBOOT_SUFFIX};;
                    esac
                fi
            done
            unset j
        done
        unset i
    fi
}

PACKAGES += "${PN}-images"
FILES_${PN}-images += "/boot"
COMPATIBLE_MACHINE = "(qoriq)"
