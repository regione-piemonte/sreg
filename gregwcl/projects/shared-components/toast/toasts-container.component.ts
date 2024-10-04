import { Component } from '@angular/core';

import { AppToastService } from './app-toast.service';


@Component({
  selector: 'app-toasts',
  templateUrl: './toasts-container.component.html',
  styleUrls: ['./toasts-container.component.css']
})
export class ToastsContainerComponent {
    
  constructor(public toastService: AppToastService) {}
}
