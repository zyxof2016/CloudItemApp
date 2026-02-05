package com.clouditemapp.presentation.ui.learning

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.clouditemapp.R
import com.clouditemapp.presentation.ui.common.WindowSizeClass
import com.clouditemapp.presentation.ui.common.rememberWindowSizeClass
import com.clouditemapp.presentation.ui.common.ResourceUtils
import com.clouditemapp.presentation.viewmodel.LearningViewModel

import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.*

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun LearningScreen(
    navController: NavController,
    viewModel: LearningViewModel = hiltViewModel()
) {
    val items by viewModel.items.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val isListening by viewModel.isListening.collectAsState()
    val speechResult by viewModel.speechResult.collectAsState()
    val recognizedText by viewModel.recognizedText.collectAsState()
    val showCategoryFinished by viewModel.showCategoryFinished.collectAsState()
    
    val context = LocalContext.current
    val currentItem = viewModel.getCurrentItem()

    var showParentGate by remember { mutableStateOf(false) }
    var mathProblem by remember { mutableStateOf(Pair(0, 0)) }
    var parentGateAnswer by remember { mutableStateOf("") }
    var showEditOptions by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            currentItem?.let { item ->
                viewModel.saveCustomImage(item.id, it, context)
            }
        }
    }

    val windowSize = rememberWindowSizeClass()
    val haptic = LocalHapticFeedback.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startVoiceRecognition()
        }
    }

    // 状态管理：PagerState 与 ViewModel 的 currentIndex 同步
    val pagerState = rememberPagerState(initialPage = 0) {
        items.size
    }

    // 当 ViewModel 中的 currentIndex 改变时（例如通过分类切换或按钮点击），更新 PagerState
    LaunchedEffect(currentIndex, selectedCategory) {
        if (pagerState.currentPage != currentIndex && items.isNotEmpty()) {
            if (currentIndex == 0) {
                pagerState.scrollToPage(0)
            } else {
                pagerState.animateScrollToPage(currentIndex)
            }
        }
    }

    // 当用户手动滑动 Pager 时，更新 ViewModel 中的 currentIndex
    LaunchedEffect(pagerState.currentPage) {
        if (items.isNotEmpty() && pagerState.currentPage != currentIndex) {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            viewModel.goToItem(pagerState.currentPage)
        }
    }

    // 点击动画
    val scale by animateFloatAsState(
        targetValue = if (isPlaying) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    LaunchedEffect(currentItem) {
        if (currentItem != null) {
            viewModel.markCurrentAsViewed()
        }
    }

    val skyGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE0F7FA),
            Color(0xFFB3E5FC)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("认物学习", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        mathProblem = Pair((5..15).random(), (5..15).random())
                        showParentGate = true 
                    }) {
                        Icon(Icons.Default.Settings, contentDescription = "家长编辑", tint = Color(0xFF0277BD))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(skyGradient)
                .padding(paddingValues)
        ) {
            if (items.isEmpty()) {
                // 加载中
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF0277BD)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 分类标签
                    CategoryTabs(
                        categories = listOf("动物世界", "美味水果", "新鲜蔬菜", "交通工具", "日常用品", "自然现象", "食物与饮料", "身体部位"),
                        selectedCategory = selectedCategory,
                        onCategorySelected = { viewModel.loadItems(it) }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (windowSize == WindowSizeClass.Expanded) {
                        // 平板宽屏布局：左右分栏
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentPadding = PaddingValues(horizontal = 32.dp),
                            pageSpacing = 24.dp
                        ) { page ->
                            val item = items[page]
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.spacedBy(24.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    ItemImage(
                                        item = item,
                                        scale = if (page == currentIndex) scale else 1f,
                                        onPlayClick = { 
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            viewModel.togglePlaying() 
                                        }
                                    )
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    ItemInfo(
                                        item = item,
                                        onPlayClick = { viewModel.togglePlaying() },
                                        isPlaying = if (page == currentIndex) isPlaying else false,
                                        isListening = if (page == currentIndex) isListening else false,
                                        speechResult = if (page == currentIndex) speechResult else null,
                                        recognizedText = if (page == currentIndex) recognizedText else "",
                                        onMicClick = {
                                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                                                if (isListening) viewModel.stopVoiceRecognition() else viewModel.startVoiceRecognition()
                                            } else {
                                                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    } else {
                        // 手机窄屏布局：使用 Pager 实现左右滑动
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            pageSpacing = 16.dp
                        ) { page ->
                            val item = items[page]
                            ItemCard(
                                item = item,
                                scale = if (page == currentIndex) scale else 1f,
                                onPlayClick = { 
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    viewModel.togglePlaying() 
                                },
                                isPlaying = if (page == currentIndex) isPlaying else false,
                                isListening = if (page == currentIndex) isListening else false,
                                speechResult = if (page == currentIndex) speechResult else null,
                                recognizedText = if (page == currentIndex) recognizedText else "",
                                onMicClick = {
                                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                                        if (isListening) viewModel.stopVoiceRecognition() else viewModel.startVoiceRecognition()
                                    } else {
                                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                    }
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 导航按钮
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { viewModel.previousItem() },
                            enabled = currentIndex > 0,
                            modifier = Modifier.size(64.dp)
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "上一个",
                                tint = if (currentIndex > 0) Color(0xFF0277BD) else Color.Gray,
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        Text(
                            text = "${currentIndex + 1} / ${items.size}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0277BD)
                        )

                        IconButton(
                            onClick = { viewModel.nextItem() },
                            enabled = currentIndex < items.size - 1,
                            modifier = Modifier.size(64.dp)
                        ) {
                            Icon(
                                Icons.Default.ArrowForward,
                                contentDescription = "下一个",
                                tint = if (currentIndex < items.size - 1) Color(0xFF0277BD) else Color.Gray,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }
            }

            // 家长验证弹窗
            if (showParentGate) {
                androidx.compose.ui.window.Dialog(onDismissRequest = { showParentGate = false }) {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("家长验证", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0277BD))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("请输入答案以进入编辑模式：", fontSize = 14.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("${mathProblem.first} + ${mathProblem.second} = ?", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(
                                value = parentGateAnswer,
                                onValueChange = { parentGateAnswer = it },
                                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                                ),
                                modifier = Modifier.width(100.dp),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = {
                                    if (parentGateAnswer.toIntOrNull() == mathProblem.first + mathProblem.second) {
                                        showParentGate = false
                                        parentGateAnswer = ""
                                        showEditOptions = true
                                    }
                                },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("确认")
                            }
                        }
                    }
                }
            }

            // 编辑选项弹窗
            if (showEditOptions) {
                androidx.compose.ui.window.Dialog(onDismissRequest = { showEditOptions = false }) {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("编辑图片", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0277BD))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("为“${currentItem?.nameCN}”更换图片", fontSize = 14.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Button(
                                onClick = {
                                    showEditOptions = false
                                    imagePickerLauncher.launch("image/*")
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64B5F6))
                            ) {
                                Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("从相册选择")
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            OutlinedButton(
                                onClick = {
                                    showEditOptions = false
                                    currentItem?.let { viewModel.resetCustomImage(it.id) }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF5350))
                            ) {
                                Icon(Icons.Default.Restore, contentDescription = null, tint = Color(0xFFEF5350))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("恢复默认图", color = Color(0xFFEF5350))
                            }
                        }
                    }
                }
            }

            // 分类学习完成弹窗
            if (showCategoryFinished) {
                androidx.compose.ui.window.Dialog(onDismissRequest = { viewModel.dismissCategoryFinished() }) {
                    Card(
                        shape = RoundedCornerShape(32.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.celebration))
                            val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)
                            
                            Box(modifier = Modifier.size(150.dp), contentAlignment = Alignment.Center) {
                                LottieAnimation(composition = composition, progress = { progress })
                                Text(text = "🎓", fontSize = 64.sp)
                            }
                            
                            Text(
                                text = "太棒了！",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0277BD)
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "你已经学完了 ${selectedCategory}！",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = Color(0xFF81C784)
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Button(
                                onClick = { viewModel.dismissCategoryFinished() },
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0277BD)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("继续加油！", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemImage(
    item: com.clouditemapp.domain.model.Item,
    scale: Float,
    onPlayClick: () -> Unit
) {
    val context = LocalContext.current
    val imageSource = ResourceUtils.getItemImageRes(context, item.imageRes, item.category, item.customImagePath)
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White)
            .clickable { onPlayClick() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .scale(scale),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageSource)
                    .crossfade(true)
                    .placeholder(R.drawable.ic_placeholder_default)
                    .error(R.drawable.ic_error_image)
                    .build(),
                contentDescription = item.nameCN,
                modifier = Modifier.fillMaxSize(),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop // 填充完全
            )
        }
    }
}

@Composable
fun ItemInfo(
    item: com.clouditemapp.domain.model.Item,
    onPlayClick: () -> Unit,
    isPlaying: Boolean,
    isListening: Boolean,
    speechResult: Boolean?,
    recognizedText: String,
    onMicClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = item.nameCN,
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0277BD),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = item.nameEN,
                fontSize = 36.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF546E7A),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 播放按钮
                IconButton(
                    onClick = onPlayClick,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF81C784))
                ) {
                    Icon(
                        if (isPlaying) Icons.Default.Pause else Icons.Default.VolumeUp,
                        contentDescription = "播放读音",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                // 跟我读按钮
                Box(contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (recognizedText.isNotEmpty()) {
                            Text(
                                text = recognizedText,
                                fontSize = 14.sp,
                                color = Color(0xFF0277BD),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        Box(contentAlignment = Alignment.BottomEnd) {
                            IconButton(
                                onClick = onMicClick,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(if (isListening) Color(0xFFEF5350) else Color(0xFF64B5F6))
                            ) {
                                Icon(
                                    if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                                    contentDescription = "跟我读",
                                    tint = Color.White,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                            
                            if (speechResult != null) {
                                Text(
                                    text = if (speechResult) "✅" else "❌",
                                    fontSize = 24.sp,
                                    modifier = Modifier
                                        .offset(x = 8.dp, y = 8.dp)
                                        .background(Color.White, CircleShape)
                                        .padding(4.dp)
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = item.descriptionCN,
                fontSize = 24.sp,
                color = Color(0xFF78909C),
                textAlign = TextAlign.Center,
                lineHeight = 36.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryTabs(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        categories.forEach { category ->
            FilterChip(
                selected = category == selectedCategory,
                onClick = { onCategorySelected(category) },
                label = {
                    Text(
                        text = category,
                        fontSize = 16.sp,
                        fontWeight = if (category == selectedCategory) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF0277BD),
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = Color(0xFF546E7A)
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@Composable
fun ItemCard(
    item: com.clouditemapp.domain.model.Item,
    scale: Float,
    onPlayClick: () -> Unit,
    isPlaying: Boolean,
    isListening: Boolean,
    speechResult: Boolean?,
    recognizedText: String,
    onMicClick: () -> Unit
) {
    val context = LocalContext.current
    val imageSource = ResourceUtils.getItemImageRes(context, item.imageRes, item.category, item.customImagePath)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // 减小整体内边距
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 图片展示区域
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // 使用 1:1 比例，消除上下留白
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .clickable { onPlayClick() }
                    .scale(scale),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(imageSource)
                        .crossfade(true)
                        .placeholder(R.drawable.ic_placeholder_default)
                        .error(R.drawable.ic_error_image)
                        .build(),
                    contentDescription = item.nameCN,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 中文名称
            Text(
                text = item.nameCN,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0277BD),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 英文名称
            Text(
                text = item.nameEN,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF546E7A),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 按钮组
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 播放按钮
                IconButton(
                    onClick = onPlayClick,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF81C784))
                ) {
                    Icon(
                        if (isPlaying) Icons.Default.Pause else Icons.Default.VolumeUp,
                        contentDescription = "播放读音",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // 跟我读按钮
                Box(contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (recognizedText.isNotEmpty()) {
                            Text(
                                text = recognizedText,
                                fontSize = 12.sp,
                                color = Color(0xFF0277BD),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        Box(contentAlignment = Alignment.BottomEnd) {
                            IconButton(
                                onClick = onMicClick,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(if (isListening) Color(0xFFEF5350) else Color(0xFF64B5F6))
                            ) {
                                Icon(
                                    if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                                    contentDescription = "跟我读",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            
                            if (speechResult != null) {
                                Text(
                                    text = if (speechResult) "✅" else "❌",
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .offset(x = 4.dp, y = 4.dp)
                                        .background(Color.White, CircleShape)
                                        .padding(2.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 描述
            Text(
                text = item.descriptionCN,
                fontSize = 18.sp,
                color = Color(0xFF78909C),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
    }
}
