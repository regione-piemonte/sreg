import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModificaUtentePopupComponent } from './modifica-utente-popup.component';

describe('ModificaUtentePopupComponent', () => {
  let component: ModificaUtentePopupComponent;
  let fixture: ComponentFixture<ModificaUtentePopupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModificaUtentePopupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModificaUtentePopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
