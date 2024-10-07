/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { AppToastService } from '@greg-shared/toast/app-toast.service';
import { GregError } from './greg-error.model';

@Injectable()
export class GregErrorService {

    constructor(private toastService: AppToastService, private router: Router) {}

    handleError(error: GregError) {
        this.toastService.showServerError(error);
    }

    handleErrorDaGestioneUtenti(error: GregError) {
        this.toastService.showServerError(error);
        this.router.navigate(['/redirect-page'], { skipLocationChange: true });
    }

    handleWarning(warn: GregError) {
        this.toastService.showWarning(warn);
    }
}