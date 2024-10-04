import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModelloCComponent } from './modelloC.component';

describe('ModelloCComponent', () => {
  let component: ModelloCComponent;
  let fixture: ComponentFixture<ModelloCComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModelloCComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModelloCComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
