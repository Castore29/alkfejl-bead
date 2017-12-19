import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ProductService} from '../../services/product.service';
import {MatSnackBar} from '@angular/material';
import {Product} from '../../models/product';

@Component({
  selector: 'app-product-edit',
  templateUrl: './product-edit.component.html',
  styleUrls: ['./product-edit.component.css']
})
export class ProductEditComponent implements OnInit {
  productForm: FormGroup;
  preview: string;
  uploadedImage: File;

  constructor(private fb: FormBuilder,
              private productService: ProductService,
              private snackBar: MatSnackBar) {
    const product = productService.editProduct;
    this.initForm(product);
  }

  ngOnInit() {
  }

  initForm(product: Product) {
    this.productForm = this.fb.group({
      id: product ? product.id : -1,
      itemNumber: [product ? product.itemNumber : '', Validators.required],
      category: [product ? product.category : '', Validators.required],
      subCategory: [product ? product.subCategory : '', Validators.required],
      productName: [product ? product.productName : '', Validators.required],
      description: [product ? product.description : '', Validators.required],
      producer: product ? product.producer : '',
      stock: [product ? product.stock : '', [Validators.required, Validators.min(0)]],
      available: product ? !!product.available : true,
      price: [product ? product.price : '', [Validators.required, Validators.min(0)]],
      discount: [product ? product.discount : 0, [Validators.required, Validators.min(0), Validators.max(100)]],
    });
  }

  onSubmit() {
    const isUpdate: boolean = this.productService.editProduct != null;
    this.productService.saveProduct(this.productForm.value, this.uploadedImage).subscribe(data => {
      let msg: string;
      if (isUpdate) {
        msg = 'Sikeres termék módosítás!';
      } else {
        msg = 'Sikeres termék felvétel!';
      }
      this.snackBar.open(msg, 'OK', {duration: 2000});
    }, err => {
      this.snackBar.open(err.error, 'OK', {duration: 2000});
    });
  }

  onReset() {
    this.productService.editProduct = null;
    this.productForm.reset();
    this.initForm(null);
    this.preview = null;
    this.uploadedImage = null;
  }

  getNumberErrorMessage(num: string): string {
    return this.productForm.controls[num].hasError('required') ? 'A mező kitöltése kötelező!' :
      this.productForm.controls[num].hasError('min') ? 'A mező minimum 0 lehet!' :
        this.productForm.controls[num].hasError('max') ? 'A mező maximum 100 lehet!' :
          '';
  }

  fileChange(event) {
    if (event.target.files && event.target.files[0]) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.preview = e.target.result;
      };
      reader.readAsDataURL(event.target.files[0]);
      this.uploadedImage = event.target.files[0];
    } else {
      this.preview = '';
      this.uploadedImage = null;
    }
  }


}
