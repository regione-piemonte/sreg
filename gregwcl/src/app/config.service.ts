/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { Injectable } from '@angular/core';
import { environment } from "@greg-app/environments/environment";

@Injectable()
export class ConfigService {

    static getBEServer(): string {
        try { 
            if ( ENV_PROPERTIES.endpoint != null ) 
                return ENV_PROPERTIES.endpoint; 
        }
        catch ( e ) { 
            return environment.endpoint; 
        }
    }
}