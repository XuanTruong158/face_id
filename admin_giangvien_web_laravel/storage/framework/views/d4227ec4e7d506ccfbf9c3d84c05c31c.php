<?php $__env->startSection('content'); ?>
<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card shadow-sm" style="background-color: #ccf5f5; border-radius:10px;">
                <div class="card-body">
                    <h4 class="text-center mb-4 fw-bold">Thêm tài khoản Sinh viên</h4>
                    
                    <form action="<?php echo e(route('students.store')); ?>" method="POST">
                        <?php echo csrf_field(); ?>
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label class="form-label">Mã sinh viên</label>
                                <input type="text" name="student_code" class="form-control" placeholder="Mã SV" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Lớp học</label>
                                <select name="class_id" class="form-select" required>
                                    <option value="">Chọn mã lớp</option>
                                    
                                </select>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">Họ và tên</label>
                            <input type="text" name="fullname" class="form-control" placeholder="Họ tên" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Tên tài khoản</label>
                            <input type="text" name="username" class="form-control" placeholder="tài khoản" required>
                        </div>

                        <div class="d-flex justify-content-end">
                            <button type="submit" class="btn btn-success me-2">Tạo tài khoản</button>
                            <a href="<?php echo e(route('adminhomes.taikhoansv')); ?>" class="btn btn-outline-secondary">Huỷ</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<?php $__env->stopSection(); ?>

<?php echo $__env->make('layouts.app', array_diff_key(get_defined_vars(), ['__data' => 1, '__path' => 1]))->render(); ?><?php /**PATH C:\Laravel\admin_giangvien\resources\views/students/create.blade.php ENDPATH**/ ?>