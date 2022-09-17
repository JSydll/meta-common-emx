#!/bin/bash
# ------------------------------------------
# Command-line interface to manage overlays.
# ------------------------------------------
readonly COMMAND="$1"
shift

print_help() {
    cat <<EOF
Description: Command-line interface to manage overlays, such as clearing the upperdir contents.

Usage: ./$0 <command>

Available commands:
    clear-upper     - Clear all contents from the upper directory (parameter: mountpoint).
    help            - Print this help.
EOF
}

if [[ "${EUID}" -ne 0 ]]; then
    echo "[ERROR] This executable can only be used with root priviledges."
    exit 1
fi

if [[ -z "${COMMAND}" ]]; then
    print_help
    exit 2
fi

function clear_upper() {
    local -r mount_path="$1"
    if [[ -z "${mount_path}" ]]; then
        echo "[ERROR] No mount path specified! Aborting."
        exit 3
    fi

    # Get mount information
    local -r mount_info="$(mount | grep "${mount_path}")"
    if [[ -z "${mount_info}" ]]; then
        echo "[INFO] No mounted overlay found on '${mount_pat}' - skipping."
        exit 0
    fi

    local -r name="$(echo "${mount_info}" | cut -d ' ' -f1)"
    local -r mountpoint="$(echo "${mount_info}" | grep -oE "on ([^ ]+)" | cut -d ' ' -f2)"
    local -r lower="$(echo "${mount_info}" | grep -oE "lowerdir=([^,]+)" | cut -d '=' -f2)"
    local -r upper="$(echo "${mount_info}" | grep -oE "upperdir=([^,]+)" | cut -d '=' -f2)"
    local -r work="$(echo "${mount_info}" | grep -oE "workdir=([^\)]+)" | cut -d '=' -f2)"

    # Unmount, clear the data and remount again.
    umount "${mountpoint}"
    rm -rf "${upper:?}"/*
    rm -rf "${work:?}"/*
    mount -t overlay "${name}" -o lowerdir="${lower}",upperdir="${upper}",workdir="${work}" "${mountpoint}"
}

case "${COMMAND}" in
    help)
        print_help
        ;;
    clear-upper)
        clear_upper "$1"
        ;;
    *)
        echo "[ERROR] Command not supported!"
        exit 4
        ;;
esac