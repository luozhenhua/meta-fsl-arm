SUMMARY = "Debug agent for Freescale CodeWarrior"
SECTION = "apptrk"
LICENSE = "Freescale-EULA"
LIC_FILES_CHKSUM = "file://EULA;md5=c9ae442cf1f9dd6c13dfad64b0ffe73f"

DEPENDS = "elfutils"

inherit kernel-arch

SRC_URI = "git://git.freescale.com/ppc/sdk/apptrk.git;branch=sdk-v2.0.x \
    file://apptrk-install-create-all-components-of-DEST.patch \
    file://no-strip.patch \
"
SRCREV = "873f44ca6b219508f738825299453d92975fb897"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = ""

CFLAGS += " -I${STAGING_INCDIR}"

do_install() {
        oe_runmake install DESTDIR=${D}
}
