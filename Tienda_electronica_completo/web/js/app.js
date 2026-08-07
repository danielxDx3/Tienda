let products = [];

const categoryIcons = {
  'Electrónica':'⚡','Teléfonos':'📱','Computadoras':'💻','Accesorios':'🎧',
  'Ropa':'👕','Hombre':'🧥','Mujer':'👗','Hogar':'🏠','Cocina':'🍳','Iluminación':'💡'
};

let categoryMeta = [];

function buildCategoryMeta(){
  const order=['Electrónica','Teléfonos','Computadoras','Accesorios','Ropa','Hombre','Mujer','Hogar','Cocina','Iluminación'];
  categoryMeta=order.map(name=>{
    const count=products.filter(p=>p.category===name || p.parent===name).length;
    return {name,icon:categoryIcons[name] || '📦',desc:`${count} producto${count===1?'':'s'}`};
  });
}

const state = {search:'',category:'Todos',maxPrice:1200,sort:'default',cart:JSON.parse(localStorage.getItem('electroshop-cart') || '[]')};

const $ = id => document.getElementById(id);
const productGrid=$('productGrid'), resultText=$('resultText'), emptyState=$('emptyState');
const money = value => new Intl.NumberFormat('en-US',{style:'currency',currency:'USD'}).format(value);
const normalize = text => text.toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g,'');

function renderCategoryCards(){
  $('categoryCards').innerHTML = categoryMeta.slice(0,5).map(c=>`<article class="category-card" data-category="${c.name}"><div class="category-icon">${c.icon}</div><h3>${c.name}</h3><p>${c.desc}</p></article>`).join('');
  document.querySelectorAll('.category-card').forEach(card=>card.addEventListener('click',()=>{setCategory(card.dataset.category);$('productos').scrollIntoView({behavior:'smooth'});}));
}

function renderFilters(){
  const cats=['Todos', ...categoryMeta.map(c=>c.name)];
  $('categoryFilters').innerHTML=cats.map((c,i)=>`<label class="filter-option"><input type="radio" name="category" value="${c}" ${i===0?'checked':''}><span>${c}</span></label>`).join('');
  document.querySelectorAll('input[name="category"]').forEach(el=>el.addEventListener('change',e=>{state.category=e.target.value;renderProducts();}));
}

function matchesCategory(p){
  if(state.category==='Todos') return true;
  return p.category===state.category || p.parent===state.category;
}

function getFiltered(){
  let list=products.filter(p=>normalize(p.name).includes(normalize(state.search)) && matchesCategory(p) && p.price<=state.maxPrice);
  if(state.sort==='price-asc') list.sort((a,b)=>a.price-b.price);
  if(state.sort==='price-desc') list.sort((a,b)=>b.price-a.price);
  if(state.sort==='name-asc') list.sort((a,b)=>a.name.localeCompare(b.name));
  return list;
}

function renderProducts(){
  const list=getFiltered();
  resultText.textContent=`${list.length} producto${list.length===1?'':'s'} encontrado${list.length===1?'':'s'}`;
  productGrid.innerHTML=list.map(p=>`<article class="product-card"><div class="product-image"><span class="product-badge">${p.parent}</span><span class="product-emoji">${p.emoji}</span></div><div class="product-info"><span class="product-category">${p.category}</span><h3>${p.name}</h3><p class="product-path">Catálogo / ${p.parent} / ${p.category}</p><div class="product-bottom"><span class="price">${money(p.price)}</span><button class="add-btn" data-add="${p.id}" title="Agregar al carrito">＋</button></div></div></article>`).join('');
  emptyState.classList.toggle('hidden',list.length>0);
  productGrid.classList.toggle('hidden',list.length===0);
  document.querySelectorAll('[data-add]').forEach(btn=>btn.addEventListener('click',()=>addToCart(Number(btn.dataset.add))));
}

function setCategory(cat){
  state.category=cat;
  const radio=[...document.querySelectorAll('input[name="category"]')].find(r=>r.value===cat);
  if(radio) radio.checked=true;
  renderProducts();
}

function clearFilters(){
  state.search='';state.category='Todos';state.maxPrice=1200;state.sort='default';
  $('searchInput').value='';$('priceRange').value=1200;$('priceValue').textContent='$1200';$('sortSelect').value='default';
  document.querySelector('input[name="category"][value="Todos"]').checked=true;renderProducts();
}

function saveCart(){localStorage.setItem('electroshop-cart',JSON.stringify(state.cart));renderCart();}
function addToCart(id){
  const item=state.cart.find(i=>i.id===id); if(item)item.qty++; else state.cart.push({id,qty:1}); saveCart(); showToast('Producto agregado al carrito');
}
function changeQty(id,delta){const item=state.cart.find(i=>i.id===id);if(!item)return;item.qty+=delta;if(item.qty<=0)state.cart=state.cart.filter(i=>i.id!==id);saveCart();}
function removeItem(id){state.cart=state.cart.filter(i=>i.id!==id);saveCart();}
function renderCart(){
  const count=state.cart.reduce((s,i)=>s+i.qty,0);$('cartCount').textContent=count;
  const rows=state.cart.map(item=>{const p=products.find(x=>x.id===item.id);return `<div class="cart-item"><div class="cart-item-icon">${p.emoji}</div><div><h4>${p.name}</h4><small>${money(p.price)}</small><div class="cart-item-actions"><button class="qty-btn" data-minus="${p.id}">−</button><strong>${item.qty}</strong><button class="qty-btn" data-plus="${p.id}">+</button><button class="remove-item" data-remove="${p.id}">Eliminar</button></div></div><strong>${money(p.price*item.qty)}</strong></div>`}).join('');
  $('cartItems').innerHTML=rows;$('cartEmpty').classList.toggle('hidden',count>0);$('cartItems').classList.toggle('hidden',count===0);
  const total=state.cart.reduce((s,i)=>{const p=products.find(x=>x.id===i.id);return s+p.price*i.qty},0);$('cartTotal').textContent=money(total);
  document.querySelectorAll('[data-minus]').forEach(b=>b.onclick=()=>changeQty(Number(b.dataset.minus),-1));
  document.querySelectorAll('[data-plus]').forEach(b=>b.onclick=()=>changeQty(Number(b.dataset.plus),1));
  document.querySelectorAll('[data-remove]').forEach(b=>b.onclick=()=>removeItem(Number(b.dataset.remove)));
}

function openCart(){ $('cartDrawer').classList.add('open');$('overlay').classList.add('active');$('cartDrawer').setAttribute('aria-hidden','false'); }
function closePanels(){ $('cartDrawer').classList.remove('open');$('filtersPanel')?.classList.remove('open');$('overlay').classList.remove('active');$('cartDrawer').setAttribute('aria-hidden','true'); }
let toastTimer;function showToast(text){$('toast').textContent=text;$('toast').classList.add('show');clearTimeout(toastTimer);toastTimer=setTimeout(()=>$('toast').classList.remove('show'),1800)}

$('searchInput').addEventListener('input',e=>{state.search=e.target.value.trim();renderProducts()});
$('priceRange').addEventListener('input',e=>{state.maxPrice=Number(e.target.value);$('priceValue').textContent=`$${state.maxPrice}`;renderProducts()});
$('sortSelect').addEventListener('change',e=>{state.sort=e.target.value;renderProducts()});
$('clearFilters').onclick=clearFilters;$('emptyClearBtn').onclick=clearFilters;
$('cartButton').onclick=openCart;$('closeCart').onclick=closePanels;$('overlay').onclick=closePanels;
$('clearCart').onclick=()=>{state.cart=[];saveCart();showToast('Carrito vaciado')};
$('checkoutBtn').onclick=()=>{if(!state.cart.length){showToast('Tu carrito está vacío');return;}showToast('Compra simulada correctamente');state.cart=[];saveCart();setTimeout(closePanels,700)};
$('mobileFilterBtn').onclick=()=>{document.querySelector('.filters-panel').classList.add('open');$('overlay').classList.add('active')};
$('menuToggle').onclick=()=>$('navLinks').classList.toggle('open');
document.querySelectorAll('.nav-links a').forEach(a=>a.onclick=()=>$('navLinks').classList.remove('open'));
$('year').textContent=new Date().getFullYear();

async function iniciarAplicacion(){
  try {
    const respuesta = await fetch('/api/productos');
    if (!respuesta.ok) throw new Error(`HTTP ${respuesta.status}`);
    products = await respuesta.json();
    buildCategoryMeta();
    $('statProducts').textContent=products.length;
    renderCategoryCards();
    renderFilters();
    renderProducts();
    renderCart();
  } catch (error) {
    console.error('No se pudo cargar el catálogo Java:', error);
    resultText.textContent='No se pudo conectar con el servidor Java';
    productGrid.innerHTML='<div class="empty-state"><span>⚠️</span><h3>Servidor Java no disponible</h3><p>Ejecuta ServidorWeb.java y abre http://localhost:8085.</p></div>';
  }
}

iniciarAplicacion();
