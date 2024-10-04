import { isDevMode } from '@angular/core';
import { HttpHeaders, HttpParams } from "@angular/common/http";
import * as uuid from 'uuid';

let myUUIDV4 = uuid.v4();

export function defineHeaders() {

    return new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Access-Control-Allow-Origin': '*',
        // responseType: 'text',
        // Authorization: 'my-auth-token',
        // 'Authorization': 'Bearer my-token', 
        // 'My-Custom-Header': 'foobar',
        // 'XSRF-TOKEN': this.cookieService.get("XSRF-TOKEN"),
        // 'X-Request-ID': uuid()
    })
}

// this just escapes undefined and other non trivial params from being posted
function escapeValue(val: any): any {
    if (isDevMode()) {
        if ((val && val.indexOf && val.indexOf(',') >= 0) || Array.isArray(val)) {
            console.warn('Mind that encoded arrays or arrays as string are not escaped', val);
        }
    }
    if (val || val === 0 || val === false) {
        return '' + val;
    } else {
        return '';
    }
}

export function formatBase64(b64: string) {
    let index = b64.indexOf(',');
    b64 = index !== -1 ? b64.slice(index + 1, b64.length - 1) : b64;
    return b64;
}

export function toHttpParams(obj: Object): HttpParams {
    return Object.getOwnPropertyNames(obj)
        .reduce((p, key) => p.set(key, obj[key]), new HttpParams());
}

// this function escape url special chars for backend. The escape of non trivial char (%,$) is now natively supported by HttpParams
export function encodeAsHttpParams(obj: any): { params: HttpParams } {
    const params = Object.getOwnPropertyNames(obj)
        .reduce((p, key) => p.set(key, escapeValue(obj[key])), new HttpParams());
    return { params };
}

/**
 * https://www.freecodecamp.org/news/30-seconds-of-code-rename-many-object-keys-in-javascript-268f279c7bfa/
 *
 * @param keysMap contains key / value pairs of your old / new object keys
 * @param obj is the object to be changed
 */
export function renameKeys(keysMap: { [key: string]: string }, obj: object) {
    // Start from the keymap, so all keys not defined will be ignored
    return Object.keys(keysMap).reduce(
        (acc, key) => ({
            ...acc,
            ...{ [keysMap[key] || key]: obj[key] }
        }),
        {}
    );
}

/**
 * Usefull for filter an array of objects based on a specific key
 *
 * @param arr Array of objects
 * @param field Key of the object usefull for comparison
 * @returns A new array without duplicates
 */
 export const onlyUniqueObjects = <T>(arr: T[], field: string): T[] => Object.values(
    arr.reduce(
        (acc, cur) => ({
            ...acc,
            [cur[field]]: cur
        }),
        {}
    )
  );
  