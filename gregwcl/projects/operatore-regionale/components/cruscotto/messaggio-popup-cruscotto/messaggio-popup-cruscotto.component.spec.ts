import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MessaggioPopupCruscottoComponent } from './messaggio-popup-cruscotto.component';

describe('MessaggioPopupCruscottoComponent', () => {
  let component: MessaggioPopupCruscottoComponent;
  let fixture: ComponentFixture<MessaggioPopupCruscottoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MessaggioPopupCruscottoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MessaggioPopupCruscottoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
