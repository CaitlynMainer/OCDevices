package ben_mkiv.ocdevices.common.blocks;

import ben_mkiv.ocdevices.OCDevices;
import ben_mkiv.ocdevices.common.integration.MCMultiPart.MCMultiPart;
import ben_mkiv.ocdevices.common.integration.MCMultiPart.MultiPartHelper;
import ben_mkiv.ocdevices.common.tileentity.ColoredTile;
import ben_mkiv.ocdevices.common.tileentity.TileEntityFlatScreen;
import ben_mkiv.ocdevices.common.tileentity.TileEntityKeyboard;
import li.cil.oc.OpenComputers;
import li.cil.oc.common.block.Keyboard;
import li.cil.oc.common.block.property.PropertyRotatable;
import mcmultipart.util.MCMPWorldWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockKeyboard extends Keyboard {
    public final static String NAME = "keyboard";
    public static Block DEFAULTITEM;

    public BlockKeyboard() {
        super();
        setRegistryName(OCDevices.MOD_ID, NAME);
        setUnlocalizedName(NAME);
        setCreativeTab(OCDevices.creativeTab);
    }

    @Override
    public li.cil.oc.common.tileentity.Keyboard createNewTileEntity(World worldIn, int meta) {
        return new TileEntityKeyboard();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return super.getBoundingBox(state, world, pos);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos){
        // hello neighbour, not going anywhere. no matter what you do....
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote)
            return ColoredTile.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);

        // from here client only, check if the keyboard has a screen connected
        TileEntityFlatScreen screen = MultiPartHelper.getScreenFromTile(world.getTileEntity(pos));
        if (screen != null) {
            if (world instanceof MCMPWorldWrapper)
                world = MCMultiPart.getRealWorld(screen);

            pos = screen.origin().getPos();
            player.openGui(OpenComputers.ID(), li.cil.oc.common.GuiType.Screen().id(), world, pos.getX(), pos.getY(), pos.getZ());

            return true;
        }

        return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
    }

    private static boolean activateScreen(World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing){
        IBlockState s = world.getBlockState(pos);
        Block b = s.getBlock();
        return b.onBlockActivated(world, pos, s, player, hand, facing, 0, 0, 0);
    }

    public static TileEntityKeyboard getTileEntity(IBlockAccess world, BlockPos pos){
        TileEntity te = world.getTileEntity(pos);
        return te instanceof TileEntityKeyboard ? (TileEntityKeyboard) te : null;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand){
        EnumFacing pitch = facing.getAxis().equals(EnumFacing.Axis.Y) ? facing : EnumFacing.NORTH;
        EnumFacing yaw = !facing.getAxis().equals(EnumFacing.Axis.Y) ? facing : EnumFacing.fromAngle(placer.rotationYaw);

        if(pitch.equals(EnumFacing.DOWN))
            yaw = yaw.getOpposite();

        return getDefaultState().withProperty(PropertyRotatable.Pitch(), pitch).withProperty(PropertyRotatable.Yaw(), yaw);
    }

}
