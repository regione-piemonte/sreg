import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModelloB1Component } from './modello-b1.component';

describe('ModelloB1Component', () => {
  let component: ModelloB1Component;
  let fixture: ComponentFixture<ModelloB1Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModelloB1Component ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModelloB1Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
