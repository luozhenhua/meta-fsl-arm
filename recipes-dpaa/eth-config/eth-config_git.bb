DESCRIPTION = "Ethernet Configuration Files"
LICENSE = "BSD & GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=8ed5eddbfbb84af5089ea94c382d423c"

SRC_URI = "git://git.freescale.com/ppc/sdk/eth-config.git;branch=sdk-v2.0.x"
SRCREV = "c1a4b3ae8e2bb6e5abe4d316e5d1f339085e8156"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = "D=${D}"

do_install() {
    oe_runmake install
    chown -R root:root ${D}
}

CLEANBROKEN = "1"
