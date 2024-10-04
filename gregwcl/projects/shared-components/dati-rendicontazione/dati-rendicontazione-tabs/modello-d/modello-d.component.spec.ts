import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModelloDComponent } from './modello-d.component';

describe('ModelloDComponent', () => {
  let component: ModelloDComponent;
  let fixture: ComponentFixture<ModelloDComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModelloDComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModelloDComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
