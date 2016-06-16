FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

require recipes-devtools/qemu/qemu.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

PROVIDES = "qemu"

python() {
    pkgs = d.getVar('PACKAGES', True).split()
    for p in pkgs:
        if 'qemu-qoriq' in p:
            d.appendVar("RPROVIDES_%s" % p, p.replace('qemu-qoriq', 'qemu'))
            d.appendVar("RCONFLICTS_%s" % p, p.replace('qemu-qoriq', 'qemu'))
            d.appendVar("RREPLACES_%s" % p, p.replace('qemu-qoriq', 'qemu'))
}

# remove not supported PACKAGECONFIG by this recipe
PACKAGECONFIG[gcrypt] = ""
PACKAGECONFIG[nettle] = ""
PACKAGECONFIG[nss] = ""

SRC_URI = "git://git.freescale.com/ppc/sdk/qemu.git;branch=sdk-v2.0.x"
SRCREV = "4b846e9b2b15660abace52dd27a406af08c4212d"

# add ptest patches
SRC_URI_append = "\
    file://add-ptest-in-makefile.patch \
    file://run-ptest \
"

S = "${WORKDIR}/git"

QEMU_TARGETS_qoriq-arm = "arm"
QEMU_TARGETS_qoriq_arm64 = "aarch64"
PACKAGECONFIG_append = " aio libusb"

DISABLE_STATIC = ""

# Append build host pkg-config paths for native target since the host may provide sdl
do_configure_prepend() {
    export PKG_CONFIG=${STAGING_DIR_NATIVE}${bindir_native}/pkg-config
}

do_configure_append () {
    if ! grep 'CONFIG_FDT=y' config-host.mak; then
         echo "CONFIG_RDMA=y" >> config_host_mak
    fi
}

# gets around qemu.inc trying to install powerpc_rom.bin
do_install_prepend() {
    touch ${WORKDIR}/powerpc_rom.bin
}

do_install_append() {
    rm ${WORKDIR}/powerpc_rom.bin
    # Prevent QA warnings about installed ${localstatedir}/run
    if [ -d ${D}${localstatedir}/run ]; then rmdir ${D}${localstatedir}/run; fi
}

FILES_${PN} += "${datadir}/qemu/"
INSANE_SKIP_${PN} += "dev-deps"

# FIXME: Avoid WARNING due missing patch for native/nativesdk
BBCLASSEXTEND = ""
