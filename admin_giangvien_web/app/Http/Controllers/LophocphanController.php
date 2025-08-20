<?php

namespace App\Http\Controllers;

use App\Models\Lophocphan;
use Illuminate\Http\Request;

class LophocphanController extends Controller
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
        return view('lophocphans.create');
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
    public function show(Lophocphan $lophocphan)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(Lophocphan $lophocphan)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, Lophocphan $lophocphan)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(Lophocphan $lophocphan)
    {
        //
    }
}
