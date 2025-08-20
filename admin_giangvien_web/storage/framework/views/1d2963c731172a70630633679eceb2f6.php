<?php $__env->startSection('content'); ?>
<div class="container mt-4">
    <div class="card shadow-sm">
     <div class="card-header d-flex justify-content-between align-items-center bg-light">
    <h5 class="mb-0">Quản lý môn học & lớp học</h5>
    <a href="<?php echo e(route('lophocphans.create')); ?>" class="btn btn-info btn-sm text-white">
        ➕ Thêm lớp học phần mới
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
                            <th>Mã môn</th>
                            <th>Tên môn </th>
                            <th>Mã lớp học </th>
                            <th>Số tín</th>
                            <th>Giáo viên phụ trách</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php $__empty_1 = true; $__currentLoopData = $monhocs; $__env->addLoop($__currentLoopData); foreach($__currentLoopData as $monhoc): $__env->incrementLoopIndices(); $loop = $__env->getLastLoop(); $__empty_1 = false; ?>    
                                <tr>
                                    <td><?php echo e($monhoc['id']); ?></td>
                                    <td><?php echo e($monhoc['name']); ?></td>
                                    <td><?php echo e($monhoc['monhoc']); ?></td>
                                    <td><?php echo e($monhoc['lophocphan']); ?></td>
                                    <td>
                                        <?php if($monhoc['status'] == 1): ?>
                                            <span class="text-success fw-bold">Đang hoạt động</span>
                                        <?php else: ?>
                                            <span class="text-danger fw-bold">Ngừng hoạt động</span>
                                        <?php endif; ?>
                                    </td>
                                    <td>
                                        <!-- Nút sửa -->
                                        <a href="<?php echo e(route('monhocs.edit', $monhoc['id'])); ?>" class="btn btn-success btn-sm">Sửa</a>

                                        <!-- Nút xóa -->
                                        <form action="<?php echo e(route('monhocs.destroy', $monhoc['id'])); ?>" 
                                            method="POST" class="d-inline-block">
                                            <?php echo csrf_field(); ?>
                                            <?php echo method_field('DELETE'); ?>
                                            <button type="submit" 
                                                    class="btn btn-sm btn-danger" 
                                                    onclick="return confirm('Bạn có chắc chắn muốn xóa lớp học này?')">
                                                <i class="bi bi-trash"></i> Xóa
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                                <?php endforeach; $__env->popLoop(); $loop = $__env->getLastLoop(); if ($__empty_1): ?>
                                <tr>
                                    <td colspan="6" class="text-center">Không có dữ liệu lớp học</td>
                                </tr>
                                <?php endif; ?>                        
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<?php $__env->stopSection(); ?>

<?php echo $__env->make('layouts.app', array_diff_key(get_defined_vars(), ['__data' => 1, '__path' => 1]))->render(); ?><?php /**PATH C:\Laravel\admin_giangvien\resources\views/adminhomes/monhoc_lophp.blade.php ENDPATH**/ ?>