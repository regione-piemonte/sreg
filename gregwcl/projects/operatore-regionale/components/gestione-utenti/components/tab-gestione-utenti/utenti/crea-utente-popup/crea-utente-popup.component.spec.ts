import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreaUtentePopupComponent } from './crea-utente-popup.component';

describe('CreaUtentePopupComponent', () => {
  let component: CreaUtentePopupComponent;
  let fixture: ComponentFixture<CreaUtentePopupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreaUtentePopupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreaUtentePopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
