import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InfoPopup } from './info-popup.component';

describe('InfoPopup', () => {
  let component: InfoPopup;
  let fixture: ComponentFixture<InfoPopup>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InfoPopup ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InfoPopup);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
