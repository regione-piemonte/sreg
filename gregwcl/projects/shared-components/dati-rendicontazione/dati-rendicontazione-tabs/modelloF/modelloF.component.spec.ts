import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ModelloFComponent } from './modelloF.component';

describe('ModelloFComponent', () => {
  let component: ModelloFComponent;
  let fixture: ComponentFixture<ModelloFComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModelloFComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModelloFComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
