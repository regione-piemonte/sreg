/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { Directive, ElementRef, HostListener, Input } from '@angular/core';

@Directive({
  selector: '[appTwoDigitDecimalNumber]'
})
export class TwoDigitDecimalNumberDirective {  
    @Input() customregex?: RegExp;
    @Input() usecustomregex?: boolean = false;
    private regex: RegExp = new RegExp(/^(\d{0,12})(\,?\d{0,2})$/g);  
    private specialKeys: Array < string > = ['Backspace', 'Tab', 'End', 'Home', 'ArrowLeft', 'ArrowRight', 'Del', 'Delete'];  
    constructor(private el: ElementRef) {}  
    @HostListener('keydown', ['$event'])  
    onKeyDown(event: KeyboardEvent) {  
        this.regex = this.usecustomregex? this.customregex : this.regex;
        // Allow Backspace, tab, end, and home keys     
        if (this.specialKeys.indexOf(event.key) !== -1) {  
            return;  
        }  
        let current: string = this.el.nativeElement.value;  
        let position = this.el.nativeElement.selectionStart;
 		if (event.key === ',' && position===0) {  
             event.preventDefault();  
        } 

        if (event.key === '.' && position===0) {  
            event.preventDefault();  
        } 
        
        if (current[0] === '0' && position == 1 && event.key !== ',') {
            event.preventDefault(); 
        } 
        
        let re = /\./gi;
        const next: string = [current.slice(0, position), event.key == 'Decimal' ? ',' : event.key, current.slice(position)].join('').replace(re, '');  
           
        if (next.length==13 && next.indexOf(',')==-1 && next.indexOf('-')==-1) {  
            event.preventDefault();  
        }  
        if (next.length==14 && next.indexOf(',')==-1) {  
            event.preventDefault();  
        }
        if (next && !String(next).match(this.regex)) {  
            event.preventDefault();  
        }  
    }  
}  
 