SUMMARY = "Reset Configuration Word"
DESCRIPTION = "Reset Configuration Word - hardware boot-time parameters for the QorIQ targets"
LICENSE = "Freescale-EULA"
LIC_FILES_CHKSUM = "file://EULA;md5=c9ae442cf1f9dd6c13dfad64b0ffe73f"

inherit deploy siteinfo

DEPENDS += "change-file-endianess-native tcl-native"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "git://git.freescale.com/ppc/sdk/rcw.git;branch=sdk-v2.0.x"
SRCREV = "1af724b64347170e9d9ba8e8f277dd30026a1a7e"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = "BOARDS=${@d.getVar('MACHINE', True).replace('-64b','').replace('-${SITEINFO_ENDIANNESS}','')} DESTDIR=${D}/boot/rcw/"

do_install () {
    oe_runmake install
}
do_install_append_ls102xa () {
    for f in `find ${D}/boot/rcw/ -name "*qspiboot*"`;do
        f_swap=`echo $f |sed -e 's/qspiboot/qspiboot_swap/'`
        tclsh ${STAGING_BINDIR_NATIVE}/byte_swap.tcl $f $f_swap 8
    done
}

do_install_append_fsl-lsch2 () {
     for f in `find ${D}/boot/rcw/ -name "*qspiboot*"`;do
         f_swap=`echo $f |sed -e 's/qspiboot/qspiboot_swap/'`
         tclsh ${STAGING_BINDIR_NATIVE}/byte_swap.tcl $f $f_swap 8
     done 
}

do_deploy () {
    install -d ${DEPLOYDIR}/rcw
    cp -r ${D}/boot/rcw/* ${DEPLOYDIR}/rcw/
}
addtask deploy after do_install

PACKAGES += "${PN}-image"
FILES_${PN}-image += "/boot"
COMPATIBLE_MACHINE = "(qoriq-arm|fsl-lsch2)"
