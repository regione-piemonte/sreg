import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OpCambioStatoPopupComponent } from './op-cambio-stato-popup.component';

describe('OpCambioStatoPopupComponent', () => {
  let component: OpCambioStatoPopupComponent;
  let fixture: ComponentFixture<OpCambioStatoPopupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OpCambioStatoPopupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OpCambioStatoPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
