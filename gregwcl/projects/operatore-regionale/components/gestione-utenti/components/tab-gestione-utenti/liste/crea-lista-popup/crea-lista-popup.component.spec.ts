import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreaListaPopupComponent } from './crea-lista-popup.component';

describe('CreaListaPopupComponent', () => {
  let component: CreaListaPopupComponent;
  let fixture: ComponentFixture<CreaListaPopupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreaListaPopupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreaListaPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
