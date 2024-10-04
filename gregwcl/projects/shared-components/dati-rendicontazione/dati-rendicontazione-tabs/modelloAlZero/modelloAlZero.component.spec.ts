import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModelloAlZero } from './modelloAlZero.component';

describe('ModelloAlZero', () => {
  let component: ModelloAlZero;
  let fixture: ComponentFixture<ModelloAlZero>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModelloAlZero ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModelloAlZero);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
