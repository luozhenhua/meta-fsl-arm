inherit kernel kernel-arch
require recipes-kernel/linux/linux-dtb.inc

SUMMARY = "Linux Kernel for Freescale QorIQ platforms"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

SRC_URI = "git://git.freescale.com/ppc/sdk/linux.git;branch=sdk-v2.0.x \
    file://0003-use-static-inline-in-ARM-lifeboot.h.patch \
    file://fix-the-compile-issue-under-gcc6.patch \
    file://only-set-vmpic_msi_feature-if-CONFIG_EPAPR_PARAVIRT-.patch \
"
SRCREV = "bd51baffc04ecc73f933aee1c3a37c8b44b889a7"

S = "${WORKDIR}/git"

DEPENDS_append = " libgcc"
# not put Images into /boot of rootfs, install kernel-image if needed
RDEPENDS_kernel-base = ""

KERNEL_CC_append = " ${TOOLCHAIN_OPTIONS}"
KERNEL_LD_append = " ${TOOLCHAIN_OPTIONS}"
KERNEL_EXTRA_ARGS += "LOADADDR=${UBOOT_ENTRYPOINT}"

ZIMAGE_BASE_NAME = "zImage-${PKGE}-${PKGV}-${PKGR}-${MACHINE}-${DATETIME}"
ZIMAGE_BASE_NAME[vardepsexclude] = "DATETIME"

SCMVERSION ?= "y"
DELTA_KERNEL_DEFCONFIG ?= ""
DELTA_KERNEL_DEFCONFIG_prepend_fsl-lsch2 = "freescale.config "
DELTA_KERNEL_DEFCONFIG_prepend_fsl-lsch3 = "freescale.config "
do_configure_prepend() {
    # copy desired defconfig so we pick it up for the real kernel_do_configure
    cp ${KERNEL_DEFCONFIG} ${B}/.config
    
    # add config fragments
    for deltacfg in ${DELTA_KERNEL_DEFCONFIG}; do
        if [ -f "${deltacfg}" ]; then
            ${S}/scripts/kconfig/merge_config.sh -m .config ${deltacfg}
        elif [ -f "${WORKDIR}/${deltacfg}" ]; then
            ${S}/scripts/kconfig/merge_config.sh -m .config ${WORKDIR}/${deltacfg}
        elif [ -f "${S}/arch/${ARCH}/configs/${deltacfg}" ]; then
            ${S}/scripts/kconfig/merge_config.sh -m .config \
                ${S}/arch/${ARCH}/configs/${deltacfg}
        fi
    done
    
    #add git revision to the local version
    if [ "${SCMVERSION}" = "y" ]; then
        # append sdk version if SDK_VERSION is defined
        sdkversion=''
        if [ -n "${SDK_VERSION}" ]; then
            sdkversion="-${SDK_VERSION}"
        fi
        head=`git --git-dir=${S}/.git rev-parse --verify --short HEAD 2> /dev/null`
        printf "%s%s%s%s" +sdk $sdkversion +g $head > ${B}/.scmversion
    fi
}

do_install_append_qoriq-arm() {
    install -m 0644 arch/${ARCH}/boot/zImage ${D}/boot/zImage-${KERNEL_VERSION}
    ln -sf  zImage-${KERNEL_VERSION} ${D}/boot/zImage
}

do_deploy_append_qoriq-arm() {
    install -m 0644 arch/${ARCH}/boot/zImage ${DEPLOYDIR}/${ZIMAGE_BASE_NAME}.bin
    ln -sf ${ZIMAGE_BASE_NAME}.bin ${DEPLOYDIR}/zImage-${MACHINE}.bin
    ln -sf ${ZIMAGE_BASE_NAME}.bin ${DEPLOYDIR}/zImage
}

FILES_kernel-image += "/boot/zImage*"
COMPATIBLE_MACHINE = "(qoriq)"
