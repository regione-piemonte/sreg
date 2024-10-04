import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModelloAllDComponent } from './modelloAllD.component';

describe('ModelloAllDComponent', () => {
  let component: ModelloAllDComponent;
  let fixture: ComponentFixture<ModelloAllDComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModelloAllDComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModelloAllDComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
