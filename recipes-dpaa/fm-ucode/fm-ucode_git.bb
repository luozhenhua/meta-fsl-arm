DESCRIPTION = "Fman microcode binary"
LICENSE = "Freescale-EULA"
LIC_FILES_CHKSUM = "file://EULA;md5=c9ae442cf1f9dd6c13dfad64b0ffe73f"

inherit deploy

SRC_URI = "git://git.freescale.com/ppc/sdk/fm-ucode.git;branch=sdk-v2.0.x"
SRCREV = "7ebea4539f6e075448a12ee8808a1fc17a558bfd"

S = "${WORKDIR}/git"

REGLEX ?= "${MACHINE}"
REGLEX_t1023 = "t1024"
REGLEX_t1040 = "t1040"
REGLEX_t1042 = "t1040"
REGLEX_b4420 = "b4860"
REGLEX_t4160 = "t4240"
REGLEX_ls1043ardb = "t2080"

do_install () {
    UCODE=`echo ${REGLEX} | sed -e 's,-.*$,,' -e 's,[a-zA-Z]*$,,'`
    install -d ${D}/boot
    install -m 644 fsl_fman_ucode_${UCODE}*.bin ${D}/boot/
}

do_deploy () {
    UCODE=`echo ${REGLEX} | sed -e 's,-.*$,,' -e 's,[a-zA-Z]*$,,'`
    install -d ${DEPLOYDIR}/
    install -m 644 fsl_fman_ucode_${UCODE}*.bin ${DEPLOYDIR}/
}
addtask deploy before do_build after do_install

PACKAGES += "${PN}-image"
FILES_${PN}-image += "/boot"
COMPATIBLE_MACHINE = "(qoriq-ppc|fsl-lsch2)"
