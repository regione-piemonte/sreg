import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModelloA2Component } from './modelloA2.component';

describe('ModelloAComponent', () => {
  let component: ModelloA2Component;
  let fixture: ComponentFixture<ModelloA2Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModelloA2Component ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModelloA2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
