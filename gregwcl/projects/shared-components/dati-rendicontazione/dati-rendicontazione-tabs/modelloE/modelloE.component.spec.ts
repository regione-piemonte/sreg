import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModelloEComponent } from './modelloE.component';

describe('ModelloAComponent', () => {
  let component: ModelloEComponent;
  let fixture: ComponentFixture<ModelloEComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModelloEComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModelloEComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
