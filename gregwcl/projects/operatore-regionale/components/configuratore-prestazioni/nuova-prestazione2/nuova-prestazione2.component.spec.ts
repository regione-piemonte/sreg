import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NuovaPrestazione2Component } from './nuova-prestazione2component';

describe('NuovaPrestazione2Component', () => {
  let component: NuovaPrestazione2Component;
  let fixture: ComponentFixture<NuovaPrestazione2Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NuovaPrestazione2Component ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NuovaPrestazione2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
