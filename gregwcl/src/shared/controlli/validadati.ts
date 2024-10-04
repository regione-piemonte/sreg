import { Injectable } from '@angular/core';
@Injectable()
export class ValidaDati {
  
  validateNumber(e: any) {
    const reg = /(^\d*\.?\d*[0-9]+\d*$)|(^[0-9]+\d*\.\d*$)/;
    if (e.type=='keypress'){
    let input = String.fromCharCode(e.charCode);
    if (!reg.test(input)) {
      e.preventDefault();
    }
    }
    else if (e.type=='paste'){
      if (!reg.test(e.clipboardData.getData('text/plain'))) {
        e.preventDefault();
      }
    }
}

 validateStringa(e: any) {
  const reg = /(^[A-Za-z0-9 .,\\/\u20AC]*$)/;
   if (e.type=='keypress'){
    let input = String.fromCharCode(e.charCode);
    if (!reg.test(input)) {
      e.preventDefault();
    }
  }
  else if (e.type=='paste'){
    if (!reg.test(e.clipboardData.getData('text/plain'))) {
      e.preventDefault();
    }
  }
}

validateSoloStringa(e: any) {
  const reg = /(^[A-Za-z \\/\u20AC]*$)/;
  if (e.type=='keypress'){
  let input = String.fromCharCode(e.charCode);
  if (!reg.test(input)) {
    e.preventDefault();
  }
  }
  else if (e.type=='paste'){
    if (!reg.test(e.clipboardData.getData('text/plain'))) {
      e.preventDefault();
    }
  }
}

validateNoSeparatore(e: any) {
  const reg = /(^[;]*$)/;
  if (e.type=='keypress'){
  let input = String.fromCharCode(e.charCode);
  if (reg.test(input)) {
    e.preventDefault();
  }
  }
  else if (e.type=='paste'){
    if (reg.test(e.clipboardData.getData('text/plain'))) {
      e.preventDefault();
    }
  }
}

}
