import {Pipe} from '@angular/core';
 
@Pipe({
    name: 'currencyFormat'
})
export class CurrencyFormat {
    transform(value: number,
        currencySign: string = '',
        decimalLength: number = 2, 
        chunkDelimiter: string = '.', 
        decimalDelimiter:string = ',',
        chunkLength: number = 3): string {

        let result = '\\d(?=(\\d{' + chunkLength + '})+' + (decimalLength > 0 ? '\\D' : '$') + ')';
        
        if(value && value != null) {
            let num;
            if(typeof value == 'string') {
                num = value;
            } else {
                num = value.toFixed(Math.max(0, ~~decimalLength));
            }
            return currencySign+(decimalDelimiter ? num.replace('.', decimalDelimiter) : num).replace(new RegExp(result, 'g'), '$&' + chunkDelimiter);
        }
        return null;
    }
}