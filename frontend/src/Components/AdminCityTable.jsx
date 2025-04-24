const AdminCityTable = ({ cities }) => {

    return (
        <>
            <div className="mt-12 w-full max-w-6xl">
                <h2 className="text-2xl font-semibold mb-4">City Management</h2>
                <table className="w-full table-auto border-collapse border rounded-xl overflow-hidden">
                    <thead>
                    <tr className="bg-[#0d1e45] text-[#d0e0ed] text-2xl">
                        <th className="border border-[#d0e0ed] px-4 py-4">Name</th>
                        <th className="border border-[#d0e0ed] px-4 py-4">Country</th>
                        <th className="border border-[#d0e0ed] px-4 py-4">Latitude</th>
                        <th className="border border-[#d0e0ed] px-4 py-4">Longitude</th>
                    </tr>
                    </thead>
                    <tbody>
                    {cities.map((city) => (
                        <tr key={city.id} className="bg-[#0d1e45] text-[#d0e0ed] hover:bg-[#273f79] transition-colors duration-200">
                            <td className="border border-[#d0e0ed] px-4 py-2">{city.name}</td>
                            <td className="border border-[#d0e0ed] px-4 py-2">{city.country}</td>
                            <td className="border border-[#d0e0ed] px-4 py-2">{city.latitude}</td>
                            <td className="border border-[#d0e0ed] px-4 py-2">{city.longitude}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>

            </div>
        </>
    )
}

export default AdminCityTable;