import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModelloA1Component } from './modelloA1.component';

describe('ModelloAComponent', () => {
  let component: ModelloA1Component;
  let fixture: ComponentFixture<ModelloA1Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModelloA1Component ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModelloA1Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
