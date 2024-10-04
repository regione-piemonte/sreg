import { NgbDateParserFormatter, NgbDateStruct } from "@ng-bootstrap/ng-bootstrap";
import {AbstractControl} from '@angular/forms';
import * as moment from 'moment';

export class MomentDateFormatter extends NgbDateParserFormatter {

    readonly DT_FORMAT = 'DD/MM/YYYY';

    parse(value: string): NgbDateStruct {
        if (value) {           
            let mdt = moment(value, this.DT_FORMAT)
        }
        return null;
    }
    format(date: NgbDateStruct): string {
        if (!date) return '';
        let mdt = moment([date.year, date.month - 1, date.day]);
        if (!mdt.isValid()) return '';
        return mdt.format(this.DT_FORMAT);
    }

    dataValidator(c: string): boolean {
   
    if ((c !== undefined && c !== '' && c != null)) {

        var month = null;
        var day = null;
        var year = null;
        // var currentTaxYear = Number(new Date().getFullYear() - 1);
        // alert(currentTaxYear);
        if (c.indexOf('/') > -1) {
            var res = c.split("/");           
            if (res.length > 1) {
                month = res[1];
                day = res[0]
                year = res[2];
            }                       
        }
        else {
            if (c.length == 8) {
                day = c.substring(0, 2);
                month = c.substring(2, 4);
                year = c.substring(4, 8);
            }            
        }
        if (isNaN(month) || isNaN(day) || isNaN(year) || year.length !== 4) {
            return false;
        } 
        month = Number(month);
        day = Number(day);
        year = Number(year);
        if (month < 1 || month > 12) { // check month range
          return false;
        }
        if (day < 1 || day > 31) {
          return false;
        }
        if ((month === 4 || month === 6 || month === 9 || month === 11) && day === 31) {
           return false;
        }
        if (month == 2) { // check for february 29th
            var isleap = (year % 4 === 0 && (year % 100 !== 0 || year % 400 === 0));
            if (day > 29 || (day === 29 && !isleap)) {
             return false;
            }
        }
         return true;
    }
   return false;
}
}