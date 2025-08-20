<?php $__env->startSection('content'); ?>
<div class="container mt-4">
    <div class="card shadow-sm">
     <div class="card-header d-flex justify-content-between align-items-center bg-light">
    <h5 class="mb-0">📋 Danh sách tài khoản giảng viên</h5>
    <a href="<?php echo e(route('giangviens.create')); ?>" class="btn btn-info btn-sm text-white">
        ➕ Thêm giảng viên mới
    </a>
</div>


        <div class="card-body">
            <!-- Ô tìm kiếm -->
            <div class="mb-3">
                <input type="text" class="form-control w-25" placeholder="🔍 Tìm kiếm giảng viên...">
            </div>

            <!-- Bảng dữ liệu -->
            <div class="table-responsive">
                <table class="table table-bordered table-striped align-middle text-center">
                    <thead class="table-info">
                        <tr>
                            <th>Mã SV</th>
                            <th>Họ tên</th>
                            <th>Lớp</th>
                            <th>Email</th>
                            <th>Trạng thái</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php $__empty_1 = true; $__currentLoopData = $giangviens; $__env->addLoop($__currentLoopData); foreach($__currentLoopData as $giangvien): $__env->incrementLoopIndices(); $loop = $__env->getLastLoop(); $__empty_1 = false; ?>    
                                <tr>
                                    <td><?php echo e($giangvien['id']); ?></td>
                                    <td><?php echo e($giangvien['name']); ?></td>
                                    <td><?php echo e($giangvien['monhoc']); ?></td>
                                    <td><?php echo e($giangvien['email']); ?></td>
                                    <td>
                                        <?php if($giangvien['status'] == 1): ?>
                                            <span class="text-success fw-bold">Đang hoạt động</span>
                                        <?php else: ?>
                                            <span class="text-danger fw-bold">Ngừng hoạt động</span>
                                        <?php endif; ?>
                                    </td>
                                    <td>
                                        <!-- Nút sửa -->
                                        <a href="<?php echo e(route('giangviens.edit', $giangvien['id'])); ?>" class="btn btn-success btn-sm">Sửa</a>

                                        <!-- Nút xóa -->
                                        <form action="<?php echo e(route('giangviens.destroy', $giangvien['id'])); ?>" 
                                            method="POST" class="d-inline-block">
                                            <?php echo csrf_field(); ?>
                                            <?php echo method_field('DELETE'); ?>
                                            <button type="submit" 
                                                    class="btn btn-sm btn-danger" 
                                                    onclick="return confirm('Bạn có chắc chắn muốn xóa giảng viên này?')">
                                                <i class="bi bi-trash"></i> Xóa
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                                <?php endforeach; $__env->popLoop(); $loop = $__env->getLastLoop(); if ($__empty_1): ?>
                                <tr>
                                    <td colspan="6" class="text-center">Không có dữ liệu giảng viên</td>
                                </tr>
                                <?php endif; ?>

                      

                 
                 
                        
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<?php $__env->stopSection(); ?>

<?php echo $__env->make('layouts.app', array_diff_key(get_defined_vars(), ['__data' => 1, '__path' => 1]))->render(); ?><?php /**PATH C:\Laravel\admin_giangvien\resources\views/adminhomes/taikhoangv.blade.php ENDPATH**/ ?>