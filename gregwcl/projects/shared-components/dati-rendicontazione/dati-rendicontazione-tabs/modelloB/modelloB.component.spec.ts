import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModelloBComponent } from './modelloB.component';

describe('ModelloBComponent', () => {
  let component: ModelloBComponent;
  let fixture: ComponentFixture<ModelloBComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModelloBComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModelloBComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
