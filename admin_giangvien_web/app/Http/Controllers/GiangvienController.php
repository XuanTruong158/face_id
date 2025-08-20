<?php

namespace App\Http\Controllers;

use App\Models\Giangvien;
use Illuminate\Http\Request;

class GiangvienController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        //
    }

    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
        return view('giangviens.create');
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        //
    }

    /**
     * Display the specified resource.
     */
    public function show(Giangvien $giangvien)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(Giangvien $giangvien)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, Giangvien $giangvien)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(Giangvien $giangvien)
    {
        //
    }
}
