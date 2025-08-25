<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\User;

class UserController extends Controller
{
    // Lấy tất cả user
    public function index() {
        return response()->json(User::all());
    }

    // Lấy user theo id
    public function show($id) {
        
        return response()->json(User::find($id));
    }

    // Thêm user
    public function store(Request $request) {
        $user = User::create($request->all());
        return response()->json($user);
    }

    // Cập nhật user
    public function update(Request $request, $id) {
        $user = User::find($id);
        $user->update($request->all());
        return response()->json($user);
    }

    // Xóa user
    public function destroy($id) {
        User::find($id)->delete();
        return response()->json(['message' => 'User deleted']);
    }
}
