import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CancPopupComponent } from './canc-popup.component';

describe('CancPopupComponent', () => {
  let component: CancPopupComponent;
  let fixture: ComponentFixture<CancPopupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CancPopupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CancPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
