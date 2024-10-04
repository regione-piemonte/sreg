import { Injectable } from '@angular/core';
// import { MatSnackBar } from '@angular/material';
import { GregError } from '@greg-app/shared/error/greg-error.model';
import { Toast } from './Toast';

const enum SNACKBAR_CLASSNAME {
  SUCCESS = 'custom-snackbar-container-info',
  ERROR = 'custom-snackbar-container-error',
  WARNING = 'custom-snackbar-container-warning'
}

const enum TEXT_COLOR {
  SUCCESS = 'textInfo btn',
  ERROR = 'textError btn',
  WARNING = 'textWarning btn'
}

const enum SNACKBAR_ICON {
  CHECK = 'fas fa-check',
  TIMES = 'fas fa-times',
  WARN  = 'fas fa-exclamation'
}

const enum SNACKBAR_DURATION {
  SHORT = 1000,
  MEDIUM = 3000,
  LONG = 5000,
  VERY_LONG = 10000
}

@Injectable({ providedIn: 'root' })
export class AppToastService {

  toasts: Toast[] = [];

  showSuccess(toastProva: any) {
    let toast = new Toast(toastProva.text, SNACKBAR_CLASSNAME.SUCCESS, SNACKBAR_ICON.CHECK, SNACKBAR_DURATION.LONG, TEXT_COLOR.SUCCESS);
    this.toasts.push(toast);
  }

  showError(toastProva: any) {
    let toast = new Toast(toastProva.text, SNACKBAR_CLASSNAME.ERROR, SNACKBAR_ICON.TIMES, SNACKBAR_DURATION.LONG, TEXT_COLOR.ERROR);
    this.toasts.push(toast);
  }

  showWarning(error: GregError) {
    let toast = new Toast(error.errorDesc, SNACKBAR_CLASSNAME.WARNING, SNACKBAR_ICON.WARN, SNACKBAR_DURATION.LONG, TEXT_COLOR.WARNING);
    this.toasts.push(toast);
  }

  showServerError(error: GregError) {
    let toast = new Toast(error.errorDesc, SNACKBAR_CLASSNAME.ERROR, SNACKBAR_ICON.TIMES, SNACKBAR_DURATION.LONG, TEXT_COLOR.ERROR);
    this.toasts.push(toast);
  }

  remove(toast: Toast) {
    this.toasts = this.toasts.filter(t => t != toast);
  }
  //TODO utilizzare snackBar di Material
  showSnackBar(){
    // this.snackBar.open("CIAO");
  }

  // constructor(private snackBar: MatSnackBar) { }
}
