#!/bin/bash
# 
# This script helps to mount/unmount devices.
#
# It is written to be as robust as possible, any failures (for example naming conflicts)
# abort the operation 

readonly MOUNT_DIR_BASE="/media"

function log()
{
    logger -st "mount_device" "$*"
}

function print_help()
{
    echo "Usage: $0 {add|remove} <device_name> (e.g. sdb1)"
}

if [[ $# -ne 2 ]]; then
    print_help
    exit 22
fi

readonly ACTION=$1
readonly DEVBASE=$2
readonly DEVICE="/dev/${DEVBASE}"

log "Processing ${DEVICE}..."

function load_device_properties()
{
    # Export device properties by treating the information from (busybox's) blkid as variables
    eval "$(/sbin/blkid "${DEVICE}" | awk '{for (i=2; i<NF; i++) printf $i " "; print $NF}')"

    if [[ -z "${LABEL}" ]] || [[ -z "${TYPE}" ]]; then
        log "Error: Could not read device properties for ${DEVICE} via blkid."
        exit 6
    fi
}

function is_mounted()
{
    mount | grep "${DEVICE}" &> /dev/null
}

function get_new_mount_point()
{
    echo "${MOUNT_DIR_BASE}/${LABEL}"
}

function get_existing_mount_point()
{
    mount | grep -m 1 "${DEVICE}" | awk '{ print $3 }'
}

function get_mount_options()
{
    local opts="rw,relatime"
    if [[ "${TYPE}" == "vfat" ]]; then
        opts+=",users,gid=100,umask=000,shortname=mixed,utf8=1,flush"
    fi
    echo "${opts}"
}

function do_mount()
{
    if is_mounted; then
        log "Warning: ${DEVICE} is already mounted!"
        exit 16
    fi
    load_device_properties

    local mount_point
    mount_point="$(get_new_mount_point)"
    if ! mkdir -p "${mount_point}"; then
        log "Mount point ${mount_point} already occupied!"
        exit 17
    fi

    if ! mount -o "$(get_mount_options)" "${DEVICE}" "${mount_point}"; then
        log "Error mounting ${DEVICE} (status = $?)"
        rmdir "${mount_point}"
        exit 5
    fi
    log "**** Mounted ${DEVICE} at ${mount_point}. ****"
}

function do_unmount()
{
    if ! is_mounted; then
        log "Warning: ${DEVICE} is not mounted (anymore)!"
        exit 0
    fi
    local mount_point
    mount_point="$(get_existing_mount_point)"
    
    umount -l "${DEVICE}"
    log "**** Unmounted ${DEVICE}. ****"
    if [[ -e "${mount_point}" ]]; then
        rmdir "${mount_point}"
        log "**** Removed ${mount_point} previously used for ${DEVICE}. ****"
    fi
}

case "${ACTION}" in
    add)
        do_mount
        ;;
    remove)
        do_unmount
        ;;
    *)
        print_help
        ;;
esac