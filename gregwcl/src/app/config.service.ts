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